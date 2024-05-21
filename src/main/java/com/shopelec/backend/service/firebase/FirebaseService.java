package com.shopelec.backend.service.firebase;

import com.google.firebase.auth.*;
import com.shopelec.backend.dto.response.UserResponseFirebase;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FirebaseService {

    public void deleteUserFirebase(String email) throws FirebaseAuthException {
        String uid = findByEmail(email).getUid();
        FirebaseAuth.getInstance().deleteUser(uid);
    }

    public UserResponseFirebase findByEmail(String email) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        return UserResponseFirebase
                .builder()
                .uid(userRecord.getUid())
                .email(userRecord.getEmail())
                .created(formatDate(userRecord.getUserMetadata().getCreationTimestamp()))
                .signedIn(formatDate(userRecord.getUserMetadata().getLastSignInTimestamp()))
                .build();
    }


    public List<UserResponseFirebase> listAllUsers() throws FirebaseAuthException {
        List<UserResponseFirebase> allUsers = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        ListUsersPage currentPage = firebaseAuth.listUsers(null);
        do {
            for (ExportedUserRecord user : currentPage.getValues()) {
                UserResponseFirebase response = UserResponseFirebase
                        .builder()
                        .email(user.getEmail())
                        .created(formatDate(user.getUserMetadata().getCreationTimestamp()))
                        .signedIn(formatDate(user.getUserMetadata().getLastSignInTimestamp()))
                        .uid(user.getUid())
                        .build();
                allUsers.add(response);
            }
            currentPage = currentPage.getNextPage();
        } while (currentPage != null);
        return allUsers;
    }

    private String formatDate(Long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(new Date(timestamp));
    }
}
