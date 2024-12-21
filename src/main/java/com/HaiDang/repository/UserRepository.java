package com.HaiDang.repository;

import com.HaiDang.model.Address;
import com.HaiDang.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    public Optional<User> findById(Long userId);
    @Query("select u from User u where u.role='customer'")
    List<User> findAllCustomer();
    @Transactional
    @Modifying
    @Query("update User u set u.isBlock=true where u.id=:userId")
    void BlockUser(@Param("userId") Long userId);
    @Transactional
    @Modifying

    @Query("update User u set u.isBlock=false where u.id=:userId")
    void unBlockUser(@Param("userId") Long userId);
    @Query("select role from User u where u.email=:email")
    String getRoleByUser(@Param("email") String email);
    @Query("select u from User u where u.role!='customer'")
    List<User> findUsersAdmin();
    @Query("select u from User u where u.role!='customer' and u.email like concat('%', :email ,'%')")
    List<User> findUserByEmail(@Param("email") String email);
    @Query("select a from Address a where a.user.id=:userId")
    List<Address> findAllAddresses(@Param("userId") Long userId);
}
