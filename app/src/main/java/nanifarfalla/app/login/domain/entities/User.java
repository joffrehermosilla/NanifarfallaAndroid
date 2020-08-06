package nanifarfalla.app.login.domain.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Entidad para los usuarios
 */

public class User {

    @SerializedName("id")
    private String mId;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("username")
    private String mUsername;
    private String mPassword;
    @SerializedName("employee_emp_no")
    private int mEmployeeEmpNo;
    @SerializedName("role_id")
    private int mRoleId;
    @SerializedName("token")
    private String mToken;

    public User(String id, String email, String password,
                int employeeEmpNo, int roleId, String token) {
        this.mEmail = email;
        this.mPassword = password;
        this.mId = id;
        this.mEmployeeEmpNo = employeeEmpNo;
        this.mRoleId = roleId;
        this.mToken = token;
    }

    public String getUsername() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getId() {
        return mId;
    }

    public int getEmployeeEmpNo() {
        return mEmployeeEmpNo;
    }

    public int getRoleId() {
        return mRoleId;
    }

    public String getToken() {
        return mToken;
    }
}
