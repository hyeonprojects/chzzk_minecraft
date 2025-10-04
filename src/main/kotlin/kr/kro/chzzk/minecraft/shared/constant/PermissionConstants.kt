package kr.kro.chzzk.minecraft.shared.constant

object PermissionConstants {
    
    // 기본 사용자 권한
    const val USER = "chzzk.user"
    const val LINK = "chzzk.link"
    const val UNLINK = "chzzk.unlink"
    const val STATUS = "chzzk.status"
    
    // 관리자 권한
    const val ADMIN = "chzzk.admin"
    const val ADMIN_STATS = "chzzk.admin.stats"
    const val ADMIN_LIST = "chzzk.admin.list"
    const val ADMIN_RELOAD = "chzzk.admin.reload"
    const val ADMIN_FORCE_UNLINK = "chzzk.admin.force-unlink"
    
    // 특수 권한
    const val BYPASS_COOLDOWN = "chzzk.bypass.cooldown"
    const val MULTIPLE_ACCOUNTS = "chzzk.multiple-accounts"
    
    // 모든 권한 목록
    val ALL_PERMISSIONS = setOf(
        USER, LINK, UNLINK, STATUS,
        ADMIN, ADMIN_STATS, ADMIN_LIST, ADMIN_RELOAD, ADMIN_FORCE_UNLINK,
        BYPASS_COOLDOWN, MULTIPLE_ACCOUNTS
    )
    
    // 기본 사용자 권한 목록
    val DEFAULT_USER_PERMISSIONS = setOf(
        USER, LINK, UNLINK, STATUS
    )
    
    // 관리자 권한 목록
    val ADMIN_PERMISSIONS = setOf(
        ADMIN, ADMIN_STATS, ADMIN_LIST, ADMIN_RELOAD, ADMIN_FORCE_UNLINK
    )
}