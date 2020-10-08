package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;
import sg.edu.nus.comp.cs3219.viz.common.util.Deserializer.ConferenceDeserializer;;

@JsonDeserialize(using = ConferenceDeserializer.class)
@Exportable(name = "Conference", nameInDB = "conference")
@Table(name = "conference", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "creator_identifier", "name" })
})
@Entity
public class Conference {

    @Id
    @GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "sg.edu.nus.comp.cs3219.viz.common.entity.UseExistingIdOtherwiseGenerateUsingIdentity")
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

//    @Embedded
//    private ConferencePK unique;

    private String description;
    private Date date;
    private boolean hasAuthorRecord;
    private boolean hasReviewRecord;
    private boolean hasSubmissionRecord;

    @Column(name = "creator_identifier")
    private String creatorIdentifier;
    @Column(name = "name")
    private String name;

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

    public Conference(){}

    public Conference(String creatorIdentifier, String name){
//        this.id = new ConferencePK(creatorIdentifier, name);
        // TODO: Add date here
        this.creatorIdentifier = creatorIdentifier;
        this.name = name;
    }

    public Conference(String creatorIdentifier, String name, String recordType){
        this.creatorIdentifier = creatorIdentifier;
        this.name = name;
        // TODO: Add date here
        if (recordType != null) {
            if (recordType.equals("AuthorRecord")) {
                this.hasAuthorRecord = true;
            }
            if (recordType.equals("ReviewRecord")) {
                this.hasReviewRecord = true;
            }
            if (recordType.equals("SubmissionRecord")) {
                this.hasSubmissionRecord = true;
            }
        }
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

//    public String getCreatorIdentifier() {
//        return this.id.getCreatorIdentifier();
//    }
//
//    public void setCreatorIdentifier(String creatorIdentifier) {
//        this.id.setCreatorIdentifier(creatorIdentifier);
//    }
//
//    public String getName() {
//        return this.id.getName();
//    }
//
//    public void setName(String name) {
//        this.id.setName(name);
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public static class ConferencePK implements Serializable {

        @Column(name = "creator_identifier")
        private String creatorIdentifier;
        @Column(name = "name")
        private String name;

        public ConferencePK(){}

        public ConferencePK(String creatorIdentifier, String name){
            this.creatorIdentifier = creatorIdentifier;
            this.name = name;
        }

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

        @Override
        public boolean equals(Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Conference.ConferencePK that = (Conference.ConferencePK) o;
            return this.creatorIdentifier.equals(that.creatorIdentifier) && (this.name == that.name);
        }

        @Override
        public int hashCode(){
            return Objects.hash(creatorIdentifier,name);
        }
    }
}