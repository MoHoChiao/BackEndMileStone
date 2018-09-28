package com.netpro.trinity.resource.admin.filesource.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.resource.admin.filesource.entity.FileSource;

@Repository  //宣告這是一個DAO類別
public interface FileSourceJPADao extends JpaRepository<FileSource, String> {
	@Query("select count(filesource)>0 from Filesource filesource where filesource.filesourcename=:filesourcename AND 1=1")
	Boolean existByName(@Param("filesourcename") String filesourcename);
	
	//file source uid field with In
	List<FileSource> findByFilesourceuidIn(List<String> uids);
	List<FileSource> findByFilesourceuidIn(List<String> uids, Sort sort);
	Page<FileSource> findByFilesourceuidIn(List<String> uids, Pageable pageable);
	//file source uid field with Not In
	List<FileSource> findByFilesourceuidNotIn(List<String> uids);
	List<FileSource> findByFilesourceuidNotIn(List<String> uids, Sort sort);
	Page<FileSource> findByFilesourceuidNotIn(List<String> uids, Pageable pageable);
	
	
	//find by name
	List<FileSource> findByFilesourcenameLikeIgnoreCase(String name);
	List<FileSource> findByFilesourcenameLikeIgnoreCase(String name, Sort sort);
	Page<FileSource> findByFilesourcenameLikeIgnoreCase(String name, Pageable pageable);

	//find by name and in category
	List<FileSource> findByFilesourcenameLikeIgnoreCaseAndFilesourceuidIn(String name, List<String> uids);
	List<FileSource> findByFilesourcenameLikeIgnoreCaseAndFilesourceuidIn(String name, Sort sort, List<String> uids);
	Page<FileSource> findByFilesourcenameLikeIgnoreCaseAndFilesourceuidIn(String name, Pageable pageable, List<String> uids);
			
	//find by name and not in category
	List<FileSource> findByFilesourcenameLikeIgnoreCaseAndFilesourceuidNotIn(String name, List<String> uids);
	List<FileSource> findByFilesourcenameLikeIgnoreCaseAndFilesourceuidNotIn(String name, Sort sort, List<String> uids);
	Page<FileSource> findByFilesourcenameLikeIgnoreCaseAndFilesourceuidNotIn(String name, Pageable pageable, List<String> uids);
}

