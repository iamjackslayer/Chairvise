package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import sg.edu.nus.comp.cs3219.viz.common.util.Deserializer.VersionDeserializer;
import sg.edu.nus.comp.cs3219.viz.common.util.Serializer.VersionSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@JsonSerialize(using = VersionSerializer.class)
@JsonDeserialize(using = VersionDeserializer.class)
@Exportable(name = "Version", nameInDB = "version")
@Entity
public class Version {

    @EmbeddedId
    private VersionPK id;
    private String creatorIdentifier;
    private String name;
    private Date date;
    private boolean hasAuthorRecord;
    private boolean hasReviewRecord;
    private boolean hasSubmissionRecord;


    public Version(){}

    public Version(VersionPK pk){
        this.creatorIdentifier = pk.dataSet;
        this.name = pk.getVersion();
        if (pk.getRecordType().equals("AuthorRecord")) {
            this.hasAuthorRecord = true;
        }
        if (pk.getRecordType().equals("ReviewRecord")) {
            this.hasReviewRecord = true;
        }
        if (pk.getRecordType().equals("SubmissionRecord")) {
            this.hasSubmissionRecord = true;
        }

        pk.recordType = "";
        this.id = pk;
    }

    public VersionPK getId(){return id;}

    public void setId(VersionPK id){this.id = id;}

    public String getCreatorIdentifier() {
        return creatorIdentifier;
    }

    public void setCreatorIdentifier(String creatorIdentifier) {
        this.creatorIdentifier = creatorIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getHasAuthorRecord() {
        return hasAuthorRecord;
    }

    public void setHasAuthorRecord(boolean hasAuthorRecord) {
        this.hasAuthorRecord = hasAuthorRecord;
    }

    public boolean getHasReviewRecord() {
        return hasReviewRecord;
    }

    public void setHasReviewRecord(boolean hasReviewRecord) {
        this.hasReviewRecord = hasReviewRecord;
    }

    public boolean getHasSubmissionRecord() {
        return hasSubmissionRecord;
    }

    public void setHasSubmissionRecord(boolean hasSubmissionRecord) {
        this.hasSubmissionRecord = hasSubmissionRecord;
    }

    @Embeddable
    public static class VersionPK implements Serializable{

        @Column(name = "data_set")
        private String dataSet;
        @Column(name = "record_type")
        private String recordType;
        @Column(name = "version")
        private String version;

        public VersionPK(){}

        public VersionPK(String dataSet, String recordType, String version){
            this.dataSet = dataSet;
            this.recordType = recordType;
            this.version = version;
        }

        public String getRecordType(){return recordType;}

        public void setRecordType(String recordType){this.recordType=recordType;}

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDataSet() {
            return dataSet;
        }

        public void setDataSet(String dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public boolean equals(Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VersionPK that = (VersionPK) o;
            return this.dataSet.equals(that.dataSet) && this.recordType.equals((that.recordType)) && (this.version == that.version);
        }

        @Override
        public int hashCode(){
            return Objects.hash(dataSet,recordType,version);
        }
    }
}