package ac.arial.liortamir.restmyorder.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Role implements Parcelable{
    public static final String DB_ROLE_ID = "roleId";
    public static final String DB_ROLE_NAME = "roleName";

    private Integer roleId;
    private String roleName;

    public Role(){}

    public Role(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Role(Parcel parcel){
        byte isRoleIdNull = parcel.readByte();
        if(isRoleIdNull == (byte)1)
            this.roleId = parcel.readInt();

        this.roleName = parcel.readString();
    }


    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Role)) return false;

        Role role = (Role)obj;
        return getRoleName().equals(role.getRoleName());
    }

    @Override
    public int hashCode() {
        return getRoleName().hashCode();
    }

    @Override
    public String toString(){
        return getRoleName();
    }

    // ***** Parcelable part *****//

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (roleId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(roleId);
        }

        dest.writeString(roleName);
    }


    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };
}
