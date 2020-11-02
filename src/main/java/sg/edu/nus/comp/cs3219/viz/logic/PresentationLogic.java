package sg.edu.nus.comp.cs3219.viz.logic;

import java.util.Date;
import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;

import java.util.List;
import java.util.Optional;

@Component
public class PresentationLogic {

    private final PresentationRepository presentationRepository;

    public PresentationLogic(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }

    public List<Presentation> findAllForUser(UserInfo userInfo) {
        return presentationRepository.findByCreatorIdentifier(userInfo.getUserEmail());
    }

    public List<Presentation> findAllPublicPresentation() {
        return presentationRepository.findByIsPublic(true);
    }

    public Optional<Presentation> findById(Long id) {
        return presentationRepository.findById(id);
    }

    public Presentation saveForUser(Presentation presentation, UserInfo userInfo) {
        Presentation newPresentation = new Presentation();
        newPresentation.setName(presentation.getName());
        newPresentation.setDescription(presentation.getDescription());
        newPresentation.setCreatorIdentifier(userInfo.getUserEmail());
        newPresentation.setIsPublic(presentation.getIsPublic());
        Date currentTime = new Date();
        newPresentation.setCreatedDate(currentTime);
        newPresentation.setLastUpdatedDate(currentTime);
        return presentationRepository.save(newPresentation);
    }

    public Presentation updatePresentation(Presentation oldPresentation, Presentation newPresentation) {
        oldPresentation.setName(newPresentation.getName());
        oldPresentation.setDescription(newPresentation.getDescription());
        Date currentTime = new Date();
        oldPresentation.setLastUpdatedDate(currentTime);
        // TODO: Add the following when it is done in frontend.
//        oldPresentation.setConference(newPresentation.getConference());
        oldPresentation.setIsPublic(newPresentation.getIsPublic());
        return presentationRepository.save(oldPresentation);
    }

    public Presentation updatePresentationConference (Presentation oldPresentation, Conference conference) {
        oldPresentation.setConference(conference);
        Date currentTime = new Date();
        oldPresentation.setLastUpdatedDate(currentTime);
        return presentationRepository.save(oldPresentation);
    }

    public Presentation updatePresentationLastUpdatedDate(Presentation presentation) {
        Date currentTime = new Date();
        presentation.setLastUpdatedDate(currentTime);
        return presentationRepository.save(presentation);
    }

    public void deleteById(Long id) {
        presentationRepository.deleteById(id);
    }
}
