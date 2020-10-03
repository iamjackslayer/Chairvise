package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.storage.repository.VersionRepository;

import java.util.List;

@Component
public class VersionLogic {

    private final VersionRepository versionRepository;

    public VersionLogic(VersionRepository versionRepository){
        this.versionRepository = versionRepository;
    }

    public List<Version> findAllForUser(UserInfo userInfo){
        return versionRepository.findById_DataSetEquals(userInfo.getUserEmail());
    }

    public List<Version> findAllForUserWithRecordType(UserInfo userInfo, String recordType){
        if (recordType.equals("AuthorRecord")) {
            return versionRepository.findById_DataSetAndHasAuthorRecord(userInfo.getUserEmail(), true);
        }
        if (recordType.equals("ReviewRecord")) {
            return versionRepository.findById_DataSetAndHasReviewRecord(userInfo.getUserEmail(), true);
        }
        if (recordType.equals("SubmissionRecord")) {
            return versionRepository.findById_DataSetAndHasSubmissionRecord(userInfo.getUserEmail(), true);
        } else {
          return null;
        }
//        return versionRepository.findById_DataSetAndId_RecordType(userInfo.getUserEmail(), recordType);
    }

    public List<Version> findAllForUserWithVersion(UserInfo userInfo, String version){
        return versionRepository.findById_DataSetAndId_Version(userInfo.getUserEmail(), version);
    }

    public Version saveForUser(Version version, UserInfo userInfo){
        List<Version> vList = versionRepository.findById_DataSetAndId_Version(userInfo.getUserEmail(), version.getId().getVersion());
        if (vList.size() == 0) {
            Version newVersion = new Version();
            Version.VersionPK newVersionID = version.getId();
            newVersionID.setDataSet(userInfo.getUserEmail());
            newVersion.setId(newVersionID);
            return versionRepository.save(newVersion);
        }
        return vList.get(0);
    }

}
