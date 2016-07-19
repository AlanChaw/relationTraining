package DealFile;

/**
 * Created by alan on 16/7/18.
 */
public class EntityPair {
    private String identifi;
    private String entityName_1;
    private String entityName_2;
    private Double strengthValue;
    private Integer relation;

    public EntityPair(String entityString){
        String[] elements = entityString.split("\t");

        this.identifi = elements[0];
        this.entityName_1 = elements[1];
        this.entityName_2 = elements[2];
        this.strengthValue = Double.valueOf(elements[3]);
        this.relation = Integer.valueOf(elements[4]);
    }

    public String getIdentifi() {
        return identifi;
    }

    public String getEntityName_1() {
        return entityName_1;
    }

    public String getEntityName_2() {
        return entityName_2;
    }

    public Double getStrengthValue() {
        return strengthValue;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setIdentifi(String identifi) {
        this.identifi = identifi;
    }

    public void setEntityName_1(String entityName_1) {
        this.entityName_1 = entityName_1;
    }

    public void setEntityName_2(String entityName_2) {
        this.entityName_2 = entityName_2;
    }

    public void setStrengthValue(Double strengthValue) {
        this.strengthValue = strengthValue;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        String entity = "identifi: " + identifi + "\t";
        entity += "entity1: " + entityName_1 + "\t";
        entity += "entity2: " + entityName_2 + "\t";
        entity += "strength: " + strengthValue + "\t";
        entity += "relation: " + relation + "\n";

        return entity;
    }
}
