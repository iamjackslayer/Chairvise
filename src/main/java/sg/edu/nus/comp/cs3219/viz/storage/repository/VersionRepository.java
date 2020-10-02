package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Version.VersionPK> {

    List<Version> findById_DataSetEquals(String dataSet);

    List<Version> findById_DataSetAndId_RecordType(String dataSet, String recordType);

    List<Version> findById_DataSetAndId_Version(String dataSet, String version);

    List<Version> findById_DataSetAndId_RecordTypeAndId_Version(String dataSet, String recordType, String version);

    void deleteAllById_DataSetEquals(String dataSet);
}
