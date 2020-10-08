package sg.edu.nus.comp.cs3219.viz.common.exception;

public class PresentationCommentNotFoundException extends RuntimeException {

  public PresentationCommentNotFoundException(Long presentationId, Long commentId) {
    super(String.format("Could not find comment %d for presentation %d", commentId, presentationId));
  }

}
