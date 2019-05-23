package pri.crm.vo;

import java.util.Date;

public class FileVo {
    private Integer fid;

    private String name;

    private String path;
    
	private long size;

    private String suffix;
    
    private Date time;

	private Integer eno;

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : suffix.trim();
    }

    public Date getTime() {
  		return time;
  	}

  	public void setTime(Date date) {
  		this.time = date;
  	}
  	
    public Integer getEno() {
        return eno;
    }

    public void setEno(Integer eno) {
        this.eno = eno;
    }
}