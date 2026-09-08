package org.example.opsflow.notification.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.opsflow.notification.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface NotificationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKey(Notification record);

    List<Notification> findByRecipientUserId(@Param("recipientUserId") Long recipientUserId,@Param("readStatus") Integer readStatus);

    Notification findByIdAndRecipientUserId(@Param("id") Long id,@Param("recipientUserId") Long recipientUserId);

    int markAsRead(@Param("id") Long id, @Param("recipientUserId") Long recipientUserId);
}
