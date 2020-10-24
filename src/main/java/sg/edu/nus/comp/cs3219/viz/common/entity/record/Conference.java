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
import sg.edu.nus.comp.cs3219.viz.common.util.Deserializer.ConferenceDeserializer;
import sg.edu.nus.comp.cs3219.viz.common.util.Serializer.ConferenceSerializer;;

@JsonDeserialize(using = ConferenceDeserializer.class)
@Exportable(name = "Conference", nameInDB = "conference")
@Table(name = "conference", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "creator_identifier", "name" })
})
@Entity
public class Conference implements Serializable{

    @Id
    @GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "sg.edu.nus.comp.cs3219.viz.common.entity.UseExistingIdOtherwiseGenerateUsingIdentity")
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

//    @Embedded
//    private ConferencePK unique;

    private String description;
    private Date date;

    private int numAuthorRecord;
    private int numReviewRecord;
    private int numSubmissionRecord;

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
        this.creatorIdentifier = creatorIdentifier;
        this.name = name;
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

    public int getNumAuthorRecord() {
        return numAuthorRecord;
    }

    public void setNumAuthorRecord(int numAuthorRecord) {
        this.numAuthorRecord = numAuthorRecord;
    }

    public int getNumReviewRecord() {
        return numReviewRecord;
    }

    public void setNumReviewRecord(int numReviewRecord) {
        this.numReviewRecord = numReviewRecord;
    }

    public int getNumSubmissionRecord() {
        return numSubmissionRecord;
    }

    public void setNumSubmissionRecord(int numSubmissionRecord) {
        this.numSubmissionRecord = numSubmissionRecord;
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