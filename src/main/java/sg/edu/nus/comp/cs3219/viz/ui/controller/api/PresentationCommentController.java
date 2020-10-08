package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationComment;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationCommentNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationSectionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.logic.PresentationCommentLogic;
import sg.edu.nus.comp.cs3219.viz.logic.PresentationLogic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PresentationCommentController extends BaseRestController {

  private final PresentationLogic presentationLogic;
  private final PresentationCommentLogic presentationCommentLogic;

  private final GateKeeper gateKeeper;

  public PresentationCommentController(PresentationLogic presentationLogic, PresentationCommentLogic presentationCommentLogic,
      GateKeeper gateKeeper) {
    this.presentationLogic = presentationLogic;
    this.presentationCommentLogic = presentationCommentLogic;
    this.gateKeeper = gateKeeper;
  }

  @GetMapping("/presentations/{presentationId}/comments")
  public List<PresentationComment> getAllCommentsForPresentation(@PathVariable Long presentationId) {
    UserInfo currentUser = gateKeeper.verifyLoginAccess();
    Presentation presentation = presentationLogic.findById(presentationId)
        .orElseThrow(() -> new PresentationNotFoundException(presentationId));
    return presentationCommentLogic.findAllForPresentation(presentation);
  }

  @PostMapping("/presentations/{presentationId}/comments")
  public ResponseEntity<?> addCommentForPresentation(@RequestBody PresentationComment presentationComment, @PathVariable Long presentationId) throws URISyntaxException{
    UserInfo currentUser = gateKeeper.verifyLoginAccess();

    Presentation presentation = presentationLogic.findById(presentationId)
        .orElseThrow(() -> new PresentationNotFoundException(presentationId));

    PresentationComment newPresentationComment = presentationCommentLogic.saveForPresentation(presentationComment, presentation, currentUser);

    return ResponseEntity
        .created(new URI("/presentations/" + presentation.getId() + "/comments/" + newPresentationComment.getId()))
        .body(newPresentationComment);
  }

  @PutMapping("/presentations/{presentationId}/comments/{commentId}")
  public ResponseEntity<?> updatePresentationComment(@PathVariable Long presentationId, @PathVariable Long commentId,
      @RequestBody PresentationComment newPresentationComment) throws URISyntaxException {
    PresentationComment oldPresentationComment = presentationCommentLogic.findById(commentId)
        .orElseThrow(() -> new PresentationCommentNotFoundException(presentationId, commentId));

    gateKeeper.verifyEditAccessForPresentationComment(oldPresentationComment);

    PresentationComment updatedPresentationComment =
        presentationCommentLogic.updatePresentationComment(oldPresentationComment, newPresentationComment);

    return ResponseEntity
        .created(new URI("/presentations/" + presentationId + "/comments/" + updatedPresentationComment.getId()))
        .body(updatedPresentationComment);
  }

}
