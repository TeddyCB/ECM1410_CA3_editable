package socialmedia;

import java.io.IOException;
import java.util.Scanner;

public class SocialMediaTest {
    public void TestSave() {
        SocialMedia platform = new SocialMedia();
        assert (platform.getNumberOfAccounts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        assert (platform.getTotalOriginalPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        assert (platform.getTotalCommentPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        assert (platform.getTotalEndorsmentPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        try {
            int id_1 = platform.createAccount("User1", "Hello User1");
            int id_2 = platform.createAccount("User2", "Hello User2");
            int id_3 = platform.createAccount("User3", "Hello User3");
            int id_4 = platform.createAccount("User4", "Hello User4");
            int id_5 = platform.createAccount("User5", "Hello User5");
            int id_6 = platform.createAccount("User6", "Hello User6");
            int id_7 = platform.createAccount("User7", "Hello User7");
            int id_8 = platform.createAccount("User8", "Hello User8");
            int id_9 = platform.createAccount("User9", "Hello User9");
        } catch (InvalidHandleException | IllegalHandleException e) {
            if (e.getMessage().equals("THIS USERNAME HAS BEEN TAKEN")) {
                System.out.println("UserName has been taken");
                //assert (false) : "IllegalHandleException has been thrown";
            } else {
                System.out.println("UserName too long");
                //assert (false) : "InvalidHandleException has been thrown";
            }
        }
        assert (platform.getNumberOfAccounts() == 9) : "number of accounts registered in the system does not equal 9";
        try {
            for (int i = 1; i < 10; i++) {
                platform.createPost("User" + i, "I feel great!");
            }
            assert (platform.getUserPosts().size() == 9) : "More posts than required";
            for (int i = 1; i < 10; i++) {
                platform.commentPost("User" + i, 0, "User" + i + " Feels great too!");
            }
            for (int i = 1; i < 10; i *= 2) {
                platform.commentPost("User" + i, 9, "User" + i + " Is pleased to know!");
            }
            assert (platform.getTotalCommentPosts() == 13) : "the number of comments in the system does not equal 13. True value: " + platform.getTotalCommentPosts();
            for (int i = 1; i < 10; i *= 2) {
                platform.changeAccountHandle("User" + i, "User" + i * 100);
                platform.endorsePost("User" + i * 100, 8 + i);
            }
            assert (platform.getTotalEndorsmentPosts() == 4) : "number of endorsements does not equal 4. True value: " + platform.getTotalEndorsmentPosts();
            //System.out.println(platform.showPostChildrenDetails(0));
            platform.savePlatform("TestSave");
        } catch (HandleNotRecognisedException | InvalidPostException | NotActionablePostException | PostIDNotRecognisedException | InvalidHandleException | IllegalHandleException | AssertionError | IOException e) {
            e.printStackTrace();
        }

    }
    public void printTerminalMenu(){
        System.out.println("""
                Please, enter one of the following:
                    - 1: To create an account.
                    - 2: To remove an account.
                    - 3: To create a post.
                    - 4: To comment on a post ( will show all posts in the system too )
                    - 5: To endorse a post ( will show all posts that can be endorsed )
                    - 6: To delete a post.
                    - 7: Show a thread.
                    - 8: Statistics
                    - 9: Save
                    - 0: Load
                    - -1: Exit
                """);
    }
    public void socialMediaTerminalProgram() {
        SocialMedia platform = new SocialMedia();
        printTerminalMenu();
        do {
            Scanner option = new Scanner(System.in);
            int p = option.nextInt();
            if (p == -1) {
                System.exit(-1);
            } else if (p == 1) {
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }

                System.out.println("Please, enter the name of the account: ");
                Scanner handle_input = new Scanner(System.in);
                String handle = handle_input.nextLine();
                System.out.println("Please, enter the description of your account: ");
                Scanner desc_input = new Scanner(System.in);
                String desc = desc_input.nextLine();
                try {
                    platform.createAccount(handle, desc);
                    System.out.println("Account created: ");
                    System.out.println(platform.showAccount(handle));
                } catch (IllegalHandleException | InvalidHandleException | HandleNotRecognisedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            } else if (p == 2) {
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }

                System.out.println("Please, enter the name of the account: ");
                Scanner handle_input = new Scanner(System.in);
                String handle = handle_input.nextLine();
                try {
                    platform.removeAccount(handle);
                    System.out.println("Account removed. ");
                } catch (HandleNotRecognisedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            } else if (p == 3) {
                assert (platform.getUsersList() != null): "No users in system";
                System.out.println("Please, enter the account you want to post on: ");
                Scanner handle_input = new Scanner(System.in);
                String handle = handle_input.nextLine();
                System.out.println("Please, enter the message you would like to post: ");
                Scanner msg_input = new Scanner(System.in);
                String msg = msg_input.nextLine();
                try {
                    platform.createPost(handle,msg);
                } catch (HandleNotRecognisedException | InvalidPostException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            } else if(p == 4){
                System.out.println("The current posts from users are: ");
                assert platform.getUserPosts() != null: "No posts in the system";
                for(Posts post: platform.getUserPosts()){
                    try {
                        System.out.println(platform.showIndividualPost(post.getPostID()));
                    } catch (PostIDNotRecognisedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("-------------------------------");
                }
                System.out.println("Please, enter the account handle that will be commenting: ");
                Scanner handle_input = new Scanner(System.in);
                String handle = handle_input.nextLine();
                System.out.println("Please, enter the post ID that you want to comment on: ");
                Scanner id_input = new Scanner(System.in);
                int id = id_input.nextInt();
                System.out.println("Please, enter the comment: ");
                Scanner comment_input = new Scanner(System.in);
                String comment = comment_input.nextLine();
                try {
                    platform.commentPost(handle,id,comment);
                } catch (HandleNotRecognisedException | PostIDNotRecognisedException | NotActionablePostException | InvalidPostException e) {
                    e.printStackTrace();
                }
                System.out.println();
                try {
                    System.out.println(platform.showPostChildrenDetails(id));
                } catch (PostIDNotRecognisedException | NotActionablePostException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            }else if(p ==5){
                System.out.println("The posts that can be endorsed are:");
                assert platform.getUserPosts() != null: "No posts in the system";
                for(Posts post: platform.getUserPosts()){
                    if(!post.isEndorsement()){
                        try {
                            System.out.println(platform.showIndividualPost(post.getPostID()));
                            System.out.println("----------------------------------");
                        } catch (PostIDNotRecognisedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("Please, enter the handle of the account to endorse the post:");
                Scanner handle_input = new Scanner(System.in);
                String handle = handle_input.nextLine();
                System.out.println("Please, enter the id of the post you want to endorse");
                Scanner id_input = new Scanner(System.in);
                try {
                    platform.endorsePost(handle, id_input.nextInt());
                } catch (HandleNotRecognisedException | PostIDNotRecognisedException | NotActionablePostException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            }else if(p == 6){
                System.out.println("The possible posts to be deleted: ");
                for(Posts post: platform.getUserPosts()){
                    if(!post.isDeleted()){
                        try {
                            System.out.println(platform.showIndividualPost(post.getPostID()));
                        } catch (PostIDNotRecognisedException e) {
                            e.printStackTrace();
                        }
                    }System.out.println("-------------------");
                }
                System.out.println("Please, enter the id of the post to be deleted");
                Scanner id = new Scanner(System.in);
                try {
                    platform.deletePost(id.nextInt());
                } catch (PostIDNotRecognisedException e) {
                    e.printStackTrace();
                }
                System.out.println("Post deleted");
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            }else if(p == 7){
                System.out.println("The current posts from users are: ");
                assert platform.getUserPosts() != null: "No posts in the system";
                for(Posts post: platform.getUserPosts()){
                    try {
                        System.out.println(platform.showIndividualPost(post.getPostID()));
                    } catch (PostIDNotRecognisedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("-------------------------------");
                }
                System.out.println("Please, enter the id of the post to expand the thread");
                Scanner id = new Scanner(System.in);
                try {
                    System.out.println(platform.showPostChildrenDetails(id.nextInt()));
                } catch (PostIDNotRecognisedException | NotActionablePostException e) {
                    e.printStackTrace();
                }
                System.out.println("Please, enter 0 to return to menu");
                Scanner exit = new Scanner(System.in);
                assert exit.nextInt() == 0: "Did not enter 0";
                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                printTerminalMenu();
            }else if(p==9){
                System.out.println("The accounts currently in the system are: ");
                for(Account account: platform.getUsersList()){
                    System.out.println(account.getHandle());
                    System.out.println("------------------");
                }
                Scanner handle = new Scanner(System.in);
                try {
                    System.out.println(platform.showAccount(handle.nextLine()));
                } catch (HandleNotRecognisedException e) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }

    public static void main(String[] args) {
        System.out.println("----------------------- Social Media Application ----------------------- ");
        System.out.println("""
                Please enter one of the following:\s
                   - 1: to create a preloaded instance of a social media and save it to TestSave.\s
                   - 2: to load an instance from a file.
                   - 3 : to create an instance of the social media from the terminal""");
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        assert (n == 1 || n == 2 || n == 3) : "Invalid integer input.";
        if (n == 1) {
            SocialMediaTest socialMediaTest = new SocialMediaTest();
            socialMediaTest.TestSave();
        } else if (n == 2) {
            System.out.println("Please, enter the file from where you wish to load from: ");
            Scanner input_filename = new Scanner(System.in);
            String filename = input_filename.nextLine();
            SocialMedia platform = new SocialMedia();
            try {
                platform.loadPlatform(filename);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            SocialMediaTest socialMediaTest = new SocialMediaTest();
            socialMediaTest.socialMediaTerminalProgram();
        }
    }
}
