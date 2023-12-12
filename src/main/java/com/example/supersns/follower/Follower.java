package com.example.supersns.follower;


import com.example.supersns.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "follower")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follower{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follower_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user")
    private User to;

    public Follower(User from, User to) {
        this.from = from;
        this.to = to;
    }

    public Follower(Long id, User from, User to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Follower follower = (Follower) o;
        return getId() != null && Objects.equals(getId(), follower.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
