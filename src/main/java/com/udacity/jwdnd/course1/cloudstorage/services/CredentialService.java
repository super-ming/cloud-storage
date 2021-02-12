package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    @Autowired
    private final CredentialMapper credentialMapper;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, AuthenticationService authenticationService,
                             EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.authenticationService = authenticationService;
        this.encryptionService = encryptionService;
    }

    public Credential getCredential(Integer credentialId){
        User user = authenticationService.getAuthenticatedUser();
        try{
            return credentialMapper.getCredential(user.getUserId(), credentialId);
        } catch (NullPointerException error){
            error.printStackTrace();
            throw error;
        }
    }

    public List<Credential> getCredentials(){
        User user = authenticationService.getAuthenticatedUser();
        return credentialMapper.getCredentials(user.getUserId());
    }

    public int addCredential(CredentialForm credentialForm){
        User user = authenticationService.getAuthenticatedUser();
        try {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            encryptionService.encryptValue(credentialForm.getCredentialPassword(), encodedKey);
            Integer newCredentialId = credentialMapper.insertCredential(new Credential(null,
                    credentialForm.getCredentialUrl(), credentialForm.getCredentialUsername(), encodedKey,
                    credentialForm.getCredentialPassword(), user.getUserId()));
            return newCredentialId;
        } catch (Exception error) {
            error.printStackTrace();
            return -1;
        }
    }

    public int editCredential(CredentialForm credentialForm){
        User user = authenticationService.getAuthenticatedUser();
        try {
            Credential credential = credentialMapper.getCredential(user.getUserId(), credentialForm.getCredentialId());
            Integer updatedStatus = credentialMapper.editCredential(credentialForm.getCredentialId(),
                    credentialForm.getCredentialUrl(), credentialForm.getCredentialUsername(), credential.getKey(),
                    credentialForm.getCredentialPassword(), user.getUserId());
            return updatedStatus;
        } catch (Exception error) {
            error.printStackTrace();
            return -1;
        }
    }

    public void deleteCredential(Integer credentialId) {
        User user = authenticationService.getAuthenticatedUser();
        credentialMapper.deleteCredential(user.getUserId(), credentialId);
    }
}
