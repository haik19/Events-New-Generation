import kotlin.Boolean
import kotlin.Int
import kotlin.String

/**
 * fired when opening a user profile
 */
public data class ProfileOpen(
  public val isLoggedIn: Boolean,
  public val userId: String
)

/**
 * fires when the user liked the image
 */
public data class PhotoLike(
  public val origin: String,
  public val isLoggedIn: Boolean,
  public val source: String,
  public val userId: String
)

/**
 * fires when the user opens the collection
 */
public data class CollectionOpen(
  public val origin: String,
  public val source: String,
  public val userId: String,
  public val collectionId: String
)

/**
 * fires when a user follows other users
 */
public data class Follow(
  public val origin: String,
  public val isLoggedIn: Boolean,
  public val source: String,
  public val userId: String,
  public val followingCount: Int
)

/**
 * fires when a user follows some hashtag
 */
public data class TagFollow(
  public val origin: String,
  public val isLoggedIn: Boolean,
  public val userId: String
)
