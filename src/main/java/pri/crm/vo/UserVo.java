package pri.crm.vo;

public class UserVo implements Cloneable{
	
	private String username;

    private String password;

    private Integer Eno;

    private Integer power;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getEno() {
        return Eno;
    }

    public void setEno(Integer Eno) {
        this.Eno= Eno;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
    
    @Override
    public Object clone(){
    	Object o=null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return o;
    }
}
