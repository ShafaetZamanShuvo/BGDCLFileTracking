package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.FileTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileTransactionRepository extends JpaRepository<FileTransaction, Long> {
    /**
     * Retrieves a list of FileTransaction objects where the oldCode matches the given argument,
     * ordered by transactionTime in descending order.
     * @param oldCode the oldCode to look for
     * @return a list of FileTransaction objects
     */
    List<FileTransaction> findByOldCodeOrderByTransactionTimeDesc(String oldCode);

    /**
     * Retrieves a list of FileTransaction objects where the oldCode matches the given argument
     * and the fromUserId is not equal to the toUserId, ordered by transactionTime in descending order.
     * @param oldCode the oldCode to look for
     * @return a list of FileTransaction objects
     */
    @Query("SELECT ft FROM FileTransaction ft WHERE ft.oldCode = :oldCode AND ft.fromUserId != ft.toUserId ORDER BY ft.transactionTime DESC")
    List<FileTransaction> findByOldCodeAndDifferentUsers(@Param("oldCode") String oldCode);

    void deleteByOldCodeAndFromDepartmentId(String fileCode, Long fromDepartmentId);
}
