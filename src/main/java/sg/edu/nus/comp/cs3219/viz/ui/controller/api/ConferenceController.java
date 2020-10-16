package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;
import sg.edu.nus.comp.cs3219.viz.common.exception.ConferenceNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.logic.ConferenceLogic;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import sg.edu.nus.comp.cs3219.viz.logic.PresentationLogic;

@RestController
public class ConferenceController extends BaseRestController {

    private final ConferenceLogic conferenceLogic;
    private final PresentationLogic presentationLogic;

    private final GateKeeper gateKeeper;

    public ConferenceController(ConferenceLogic conferenceLogic,
                                PresentationLogic presentationLogic,
                                GateKeeper gateKeeper) {
        this.conferenceLogic = conferenceLogic;
        this.presentationLogic = presentationLogic;
        this.gateKeeper = gateKeeper;
    }

    @GetMapping("/conferences")
    public List<Conference> all() {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();

        return conferenceLogic.findAllForUser(currentUser);
    }

    @GetMapping("/presentations/{presentationId}/conferences")
    public List<Conference> conferencesForPresentation(@PathVariable Long presentationId) {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();
        Presentation presentation = presentationLogic.findById(presentationId)
            .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        if (presentation.getCreatorIdentifier().equals(currentUser.getUserEmail())) {
            return all();
        }
        List<Conference> cList = new ArrayList<Conference>();
        cList.add(presentation.getConference());
        return cList;
    }

    @PostMapping("/conferences")
    public ResponseEntity<?> newConference(@RequestBody Conference conference) throws URISyntaxException {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();

        Conference newConference = conferenceLogic.saveForUser(conference, currentUser);

        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(newConference);
    }

    @GetMapping("/conferences/{id}")
    public Conference one(@PathVariable Long id) {
        Conference conference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        //gateKeeper.verifyAccessForConference(conference, AccessLevel.CAN_READ);

        return conference;
    }

    @PutMapping("/conferences/{id}")
    public ResponseEntity<?> updateConference(@RequestBody Conference newConference, @PathVariable Long id) throws URISyntaxException {

        Conference oldConference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        //gateKeeper.verifyAccessForConference(oldConference, AccessLevel.CAN_WRITE);

        Conference updatedConference = conferenceLogic.updateConference(oldConference, newConference);
        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(updatedConference);
    }

    @DeleteMapping("/conferences/{id}")
    public ResponseEntity<?> deleteConference(@PathVariable Long id) {
        Conference oldConference = conferenceLogic.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        //gateKeeper.verifyDeletionAccessForConference(oldConference);

        conferenceLogic.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
