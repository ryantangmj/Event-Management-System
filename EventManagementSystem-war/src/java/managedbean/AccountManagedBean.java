/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Account;
import entity.Event;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.AccountSessionLocal;

/**
 *
 * @author admin
 */
@Named(value = "accountManagedBean")
@ViewScoped
public class AccountManagedBean implements Serializable {

    @EJB
    private AccountSessionLocal accountSession;

    private Long accountId;
    private Account account;
    private String name;
    private String contactDetails;
    private String email;
    private String password;
    private String validatePassword;
    private Part uploadedfile;
    private String filename = "";
    private byte[] fileContent;
    private String error;
    private List<Event> organisedEvents;
    private List<Event> joinedEvents;

    public AccountManagedBean() {
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public Part getUploadedfile() {
        return uploadedfile;
    }

    public void setUploadedfile(Part uploadedfile) {
        this.uploadedfile = uploadedfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<Event> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(List<Event> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public List<Event> getOrganisedEvents() {
        return organisedEvents;
    }

    public void setOrganisedEvents(List<Event> organisedEvents) {
        this.organisedEvents = organisedEvents;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getValidatePassword() {
        return validatePassword;
    }

    public void setValidatePassword(String validatePassword) {
        this.validatePassword = validatePassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void loadSelectedCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.account = accountSession.getAccount(accountId);
            name = this.account.getName();
            contactDetails = this.account.getContactDetails();
            email = this.account.getEmail();
            password = this.account.getPassword();
            validatePassword = password;
            fileContent = this.account.getProfilePicContent();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load account"));
        }
    }

    public void editAccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        account.setName(name);
        account.setContactDetails(contactDetails);
        account.setEmail(email);
        account.setPassword(password);
        account.setProfilePicContent(fileContent);

        try {
            accountSession.updateAccount(account);
            context.addMessage(null, new FacesMessage("Success", "Successfully updated details"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage("Error", "Failed to update account details"));
        }
    }

    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }

        return output.toByteArray();
    }

    public void upload() throws IOException {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        //get the deployment path
        String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // This will create the directory and any necessary parent directories.
        }
        System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

        //debug purposes
        setFilename(Paths.get(uploadedfile.getSubmittedFileName()).getFileName().toString());
        System.out.println("filename: " + getFilename());
        //---------------------

        //replace existing file
        Path path = Paths.get(UPLOAD_DIRECTORY + getFilename());
        InputStream bytes = uploadedfile.getInputStream();
        Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);

        String fileName = Paths.get(uploadedfile.getSubmittedFileName()).getFileName().toString();
        String contentType = uploadedfile.getContentType();
        fileContent = toByteArray(uploadedfile.getInputStream());
    }

    public String createAccount() {
        if (accountSession.sameEmail(email)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "This email already has a registered account"));
            return "createAccount.xhtml";
        }
        
        Account account = new Account();
        account.setName(name);
        account.setContactDetails(contactDetails);
        account.setEmail(email);
        account.setPassword(password);
        account.setProfilePicContent(fileContent);

        accountSession.createAccount(account);
        return "index.xhtml?faces-redirect=true";
    }
}
