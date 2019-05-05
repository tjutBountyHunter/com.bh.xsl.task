package xsl.pojo.common;

/**
 * MonitorNode 扇形图使用的节点
 */
public class MonitorNode {
    private Integer value;
    private String name;

    public MonitorNode( Integer value,String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
