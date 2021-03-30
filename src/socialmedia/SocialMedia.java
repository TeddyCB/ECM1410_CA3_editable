package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

public class SocialMedia implements SocialMediaPlatform {
    private ArrayList<Account> usersList = new ArrayList<>();

    public ArrayList<Account> getUsersList() {
        return usersList;
    }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        return 0;
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {

    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle) throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {

    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        return null;
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        return 0;
    }

    @Override
    public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        return 0;
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        return 0;
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {

    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        return null;
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {
        return null;
    }

    @Override
    public int getMostEndorsedPost() {
        return 0;
    }

    @Override
    public int getMostEndorsedAccount() {
        return 0;
    }

    @Override
    public void erasePlatform() {

    }

    @Override
    public void savePlatform(String filename) throws IOException {

    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {

    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        Account user = new Account();
        int id;
        if(usersList == null){
            id = 0;
        }else{
            id = usersList.size();
        }
        user.createAccount(handle,description, id);
        usersList.add(user);
        return 0;
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        int user_index;
        for(int i = 0; i < usersList.size(); i++){
            Account user = usersList.get(i);
            if(user.getHandle().equals(handle)){
                user_index = i;
                usersList.remove(user_index);
                break;
            }
        }

    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {

    }

    @Override
    public int getNumberOfAccounts() {
        return 0;
    }

    @Override
    public int getTotalOriginalPosts() {
        return 0;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        return 0;
    }

    @Override
    public int getTotalCommentPosts() {
        return 0;
    }

    public static void main(String[] args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException {
        SocialMedia socialMedia = new SocialMedia();
        socialMedia.createAccount("User1","Hello, user1");
        socialMedia.createAccount("User2", "Hello, user2");
        System.out.println(socialMedia.getUsersList().get(0).getId() + " " + socialMedia.getUsersList().get(0).getHandle());
        System.out.println(socialMedia.getUsersList().get(1).getId() +" " + socialMedia.getUsersList().get(1).getHandle());
        socialMedia.removeAccount("User2");
        System.out.println(socialMedia.getUsersList().size());

    }
}