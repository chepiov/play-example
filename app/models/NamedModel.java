package models;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import controllers.json.OffsetDateTimeSerializer;
import controllers.json.view.ActionsView;
import controllers.json.view.NamedViews;
import controllers.json.StateSerializer;
import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Common part of entities representation.
 * Used as "table per entity" skeleton.
 */
@MappedSuperclass
public abstract class NamedModel extends Model {

    @JsonView({NamedViews.Public.class, ActionsView.CreateView.class})
    @Id
    public UUID id;

    @JsonView({NamedViews.Internal.class, ActionsView.CreateView.class})
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @WhenCreated
    public OffsetDateTime whenCreated;

    @JsonView({NamedViews.Internal.class, ActionsView.UpdateView.class})
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @WhenModified
    public OffsetDateTime whenUpdated;

    @JsonView(NamedViews.Internal.class)
    @Version
    public long version;

    @JsonView(NamedViews.Internal.class)
    @JsonSerialize(using = StateSerializer.class)
    @SoftDelete
    public boolean state;

}
