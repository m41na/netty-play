package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_questions")
public class Question extends ValidatableEntity {

    public enum Category {math, science, geography}

    @Id
    @GeneratedValue
    @NotNull(message = "Id value cannot be null")
    public final Long id;
    @NotNull(message = "Question content cannot be null")
    @Size(min = 8, max = 128, message = "Question content length should between 8 and 256")
    public final String content;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Question category cannot be null")
    public final Category category;
    @NotNull(message = "Asked by cannot be null")
    @ManyToOne(targetEntity = User.class)
    public final Long askedBy;
    @NotNull(message = "Answer to question should not be null")
    @Size(min = 8, max = 128, message = "Answer to the question should be between 8 and 128 in length")
    public final String correctAnswer;
    @NotNull(message = "Reason for answer should not be null")
    @Size(min = 8, max = 256, message = "Reason for answer should be between 8 and 128 in length")
    public final String answerReason;
    @NotNull(message = "Date created cannot be null")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public Question(Long id, String content, Category category, Long askedBy, String correctAnswer, String answerReason, Date dateCreated) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.askedBy = askedBy;
        this.correctAnswer = correctAnswer;
        this.answerReason = answerReason;
        this.dateCreated = dateCreated;
    }
}
