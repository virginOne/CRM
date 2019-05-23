package pri.crm.vo;

public class RelationshipVo {
    private Integer rid;

    private String cid1;

    private String cid2;

    private String relationship;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getCid1() {
        return cid1;
    }

    public void setCid1(String cid1) {
        this.cid1 = cid1 == null ? null : cid1.trim();
    }

    public String getCid2() {
        return cid2;
    }

    public void setCid2(String cid2) {
        this.cid2 = cid2 == null ? null : cid2.trim();
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship == null ? null : relationship.trim();
    }
}