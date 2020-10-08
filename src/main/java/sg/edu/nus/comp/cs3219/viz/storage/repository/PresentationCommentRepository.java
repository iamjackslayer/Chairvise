package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;

import java.util.List;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationComment;

public interface PresentationCommentRepository extends JpaRepository<PresentationComment, Long> {

  List<PresentationComment> findByPresentation(Presentation presentation);
}
