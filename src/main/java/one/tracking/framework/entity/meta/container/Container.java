/**
 *
 */
package one.tracking.framework.entity.meta.container;

import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import one.tracking.framework.entity.meta.question.Question;

/**
 * @author Marko Voß
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "CONTAINER_TYPE", discriminatorType = DiscriminatorType.STRING, length = 9)
@Entity
public class Container {

  @Id
  @GeneratedValue
  private Long id;

  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
  @OrderBy("ranking ASC")
  @JoinColumn(name = "container_id")
  private List<Question> questions;

  @ManyToOne(fetch = FetchType.LAZY)
  private Question parent;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Formula("CONTAINER_TYPE")
  private String typeString;

  @Transient
  @Setter(AccessLevel.NONE)
  private ContainerType type;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @PostLoad
  void onPostLoad() {
    this.type = ContainerType.valueOf(this.typeString);
  }

  @PrePersist
  protected void onPrePersist() {
    if (this.id == null) {
      setCreatedAt(Instant.now());
    }
  }
}
