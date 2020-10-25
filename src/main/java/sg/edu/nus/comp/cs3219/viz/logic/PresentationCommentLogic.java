package sg.edu.nus.comp.cs3219.viz.logic;

import com.google.appengine.api.users.User;
import java.util.Date;
import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationComment;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationCommentRepository;

import java.util.List;
import java.util.Optional;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;

@Component
public class PresentationCommentLogic {

  private final PresentationCommentRepository presentationCommentRepository;

  public PresentationCommentLogic(PresentationCommentRepository presentationCommentRepository) {
    this.presentationCommentRepository = presentationCommentRepository;
  }

  public Optional<PresentationComment> findById(Long id) {
    return presentationCommentRepository.findById(id);
  }

  public List<PresentationComment> findAllForPresentation(Presentation presentation) {
    return presentationCommentRepository.findByPresentation(presentation);
  }

  public PresentationComment saveForPresentation(PresentationComment presentationComment, Presentation presentation, UserInfo userInfo) {
    PresentationComment newComment = new PresentationComment();
    newComment.setComment(presentationComment.getComment());
    newComment.setUserIdentifier(userInfo.getUserEmail());
    newComment.setPresentation(presentation);
    Date currentTime = new Date();
    newComment.setCreatedDate(currentTime);
    newComment.setLastUpdatedDate(currentTime);
    return presentationCommentRepository.save(newComment);
  }

  public PresentationComment updatePresentationComment(PresentationComment oldPresentationComment, PresentationComment newPresentationComment) {
    oldPresentationComment.setComment(newPresentationComment.getComment());
    Date currentTime = new Date();
    oldPresentationComment.setLastUpdatedDate(currentTime);
    return presentationCommentRepository.save(oldPresentationComment);
  }

}
