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


    public List<UserResponseFirebase> listAllUsers(int page, int pageSize) throws FirebaseAuthException {
        List<UserResponseFirebase> allUsers = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        int offset = page * pageSize;

        ListUsersPage currentPage = firebaseAuth.listUsers(null);
        int index = 0;
        do {
            for (UserRecord user : currentPage.getValues()) {
                if (index >= offset && index < offset + pageSize) {
                    UserResponseFirebase response = new UserResponseFirebase();
                    response.setEmail(user.getEmail());
                    response.setCreated(formatDate(user.getUserMetadata().getCreationTimestamp()));
                    response.setSignedIn(formatDate(user.getUserMetadata().getLastSignInTimestamp()));
                    response.setUid(user.getUid());
                    allUsers.add(response);
                }
                index++;
            }
            currentPage = currentPage.getNextPage();
        } while (currentPage != null && allUsers.size() < offset + pageSize);
        return allUsers;
    }

    public int getTotalUsersCount() throws FirebaseAuthException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        int totalCount = 0;
        ListUsersPage page = firebaseAuth.listUsers(null);

        while (page != null) {
            Iterable<ExportedUserRecord> users = page.getValues();
            for (UserRecord user : users) {
                totalCount++;
            }
            page = page.getNextPage();
        }
        return totalCount;
    }

    private String formatDate(Long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(new Date(timestamp));
    }
}
