package com.cg.repository;

import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.staff.StaffFilterReqDTO;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {

    Optional<Staff> findByUser(User user);

    Optional<Staff> findByPhone(String phone);

    Boolean existsByPhoneAndIdNot(String phone, Long id);

    @Query("SELECT " +
            "sf " +
            "FROM Staff AS sf " +
            "JOIN User AS us " +
            "ON sf.user = us " +
            "AND us.username = :username"
    )
    Optional<Staff> findByUsername(@Param("username") String username);

    @Query("SELECT " +
                "sf.fullName " +
            "FROM Staff AS sf " +
            "JOIN User AS us " +
            "ON sf.user = us " +
            "AND us.username = :username"
    )
    String getFullNameByUsername(@Param("username") String username);


    @Modifying
    @Query("UPDATE Staff AS sf SET sf.deleted = true WHERE sf.id = :staffId")
    void softDelete(@Param("staffId") long staffId);

    default Page<Staff> findAll(StaffFilterReqDTO staffFilterReqDTO, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String keyWord = staffFilterReqDTO.getKeyWord();
            Integer roleIdFilter = staffFilterReqDTO.getRoleIdFilter();

            Path isDeleted = root.get("deleted");
            if (isDeleted !=null) {
                Predicate predicate = criteriaBuilder.isFalse(root.get("deleted"));
                predicates.add(predicate);
            }

            if (keyWord != null) {
                Predicate predicateFullName = criteriaBuilder.like(root.get("fullName"), '%' + keyWord + '%');
                Predicate predicatePhone = criteriaBuilder.like(root.get("phone"), '%' + keyWord + '%');
                Predicate predicateKw = criteriaBuilder.or(predicateFullName, predicatePhone);
                predicates.add(predicateKw);
            }

            if (roleIdFilter != null && roleIdFilter!= -1) {
                Predicate predicate = criteriaBuilder.equal(root.get("user").get("role").get("id"), roleIdFilter);
                predicates.add(predicate);
            }

            Path<Instant> birthdayPath = root.get("dob");
            Integer dayOfBirthDay = staffFilterReqDTO.getDayOfBirthday();
            Expression<Integer> dayOfBirthDayFunction = criteriaBuilder.function("day", Integer.class, birthdayPath);

            if (dayOfBirthDay != null) {
                Predicate predicate = criteriaBuilder.equal(dayOfBirthDayFunction, dayOfBirthDay);
                predicates.add(predicate);
            }

            Integer monthOfBirthday = staffFilterReqDTO.getMonthOfBirthday();
            Expression<Integer> monthOfBirthdayFunction = criteriaBuilder.function("month", Integer.class, birthdayPath);

            if (monthOfBirthday != null) {
                Predicate predicate = criteriaBuilder.equal(monthOfBirthdayFunction, monthOfBirthday);
                predicates.add(predicate);
            }

            Date birthDayFrom = staffFilterReqDTO.getBirthDayFrom();
            Date birthDayTo = staffFilterReqDTO.getBirthDayTo();

            if (birthDayFrom != null && birthDayTo != null) {
                Predicate predicate = criteriaBuilder.between(root.get("dob"), birthDayFrom.toInstant(), birthDayTo.toInstant());
                predicates.add(predicate);
            }
            else {
                if (birthDayFrom != null) {
                    Predicate predicate = criteriaBuilder.greaterThan(root.get("dob"), birthDayFrom.toInstant());
                    predicates.add(predicate);
                }

                if (birthDayTo != null) {
                    Predicate predicate = criteriaBuilder.lessThan(root.get("dob"), birthDayTo.toInstant());
                    predicates.add(predicate);
                }
            }

            Date birthDay = staffFilterReqDTO.getBirthDay();

            if (birthDay != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("dob"), birthDay.toInstant());
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }

    @Query("SELECT NEW com.cg.domain.dto.staff.StaffCountDTO (" +
            "count(st.id) " +
            ") " +
            "FROM Staff AS st " +
            "WHERE st.deleted = false "
    )
    StaffCountDTO countStaff ();


}
