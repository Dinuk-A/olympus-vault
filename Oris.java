public class Oris {
    public static void createNewProfile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Profile Creation ===");

        // Collect new username
        System.out.print("Enter a new username: ");
        String newUsername = scanner.nextLine();

        // Collect new password
        System.out.print("Enter a password: ");
        String newPassword = scanner.nextLine();

        // Validate username uniqueness
        JSONArray userList = getUserList();
        for (int i = 0; i < userList.length(); i++) {
            JSONObject user = userList.getJSONObject(i);
            if (user.getString("username").equals(newUsername)) {
                System.out.println("Username already exists. Please try again.");
                return; // Exit this method and go back to the username check
            }
        }

        // Create a new JSON object for the profile
        JSONObject newUser = new JSONObject();
        newUser.put("username", newUsername);
        newUser.put("password", newPassword);

        // Add to the user list
        userList.put(newUser);

        // Save back to the file
        saveUserList(userList);

        System.out.println("Profile created successfully!");
    }
}
