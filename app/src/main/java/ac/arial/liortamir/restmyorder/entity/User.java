package ac.arial.liortamir.restmyorder.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    public static final String DB_USER_ID = "userId";
    public static final String DB_USER_FULL_NAME = "fullName";
    public static final String DB_USER_EMAIL = "email";
    public static final String DB_USER_PASSWORD = "password";
    public static final String DB_USER_ROLE_ID = "roleId";

    private Integer userId;
    private String fullName;
    private String email;
    private String password;
    private Role role;

    public User(){}

    public User(Integer userId, String fullName, String email, String password, Role role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Parcel parcel){

        this.role = parcel.readParcelable(Role.class.getClassLoader());
        byte isUserIdNull = parcel.readByte();
        if(isUserIdNull == 1)
            userId = parcel.readInt();
        String[] arr = new String[3];
        parcel.readStringArray(arr);
        this.fullName = arr[0];
        this.email = arr[1];
        this.password = arr[2];


    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    // ***** Parcelable part *****//


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        parcel.readParcelable(Role.class.getClassLoader());
        dest.writeParcelable(role, flags);
        if(userId == null) {
            dest.writeByte((byte) 0);
        }else{
            dest.writeByte((byte)1);
            dest.writeInt(userId);
        }
        dest.writeStringArray(new String[]{fullName, email, password});

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
