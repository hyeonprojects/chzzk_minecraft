package kr.kro.chzzk.minecraft.shared.extension

import org.bukkit.entity.Player
import kr.kro.chzzk.minecraft.shared.constant.PermissionConstants

/**
 * Player 클래스를 위한 확장 함수들
 * 
 * 마인크래프트 Player 객체에 치지직 플러그인 특화 기능들을 추가합니다.
 * Kotlin의 확장 함수를 활용하여 기존 API를 확장하고 코드의 가독성을 높입니다.
 * 
 * 제공하는 기능:
 * - 치지직 권한 검사 단축 함수들
 * - 전용 메시지 송신 함수들
 * - 플레이어 표시명 포맷팅
 * - 권한 조합 검사
 * 
 * @author Hyeonprojects
 * @since 1.0
 */

/**
 * 플레이어가 특정 치지직 권한을 가지고 있는지 확인합니다.
 * 
 * @param permission 확인할 권한
 * @return Boolean 권한이 있으면 true
 */
fun Player.hasChzzkPermission(permission: String): Boolean {
    return this.hasPermission(permission)
}

/**
 * 플레이어가 지정된 권한들 중 하나라도 가지고 있는지 확인합니다.
 * 
 * @param permissions 확인할 권한들
 * @return Boolean 권한 중 하나라도 있으면 true
 */
fun Player.hasAnyChzzkPermission(vararg permissions: String): Boolean {
    return permissions.any { this.hasPermission(it) }
}

/**
 * 플레이어가 지정된 모든 권한을 가지고 있는지 확인합니다.
 * 
 * @param permissions 확인할 권한들
 * @return Boolean 모든 권한이 있으면 true
 */
fun Player.hasAllChzzkPermissions(vararg permissions: String): Boolean {
    return permissions.all { this.hasPermission(it) }
}

/**
 * 플레이어가 치지직 관리자 권한을 가지고 있는지 확인합니다.
 * 
 * @return Boolean 관리자 권한이 있거나 OP이면 true
 */
fun Player.isChzzkAdmin(): Boolean {
    return this.hasPermission(PermissionConstants.ADMIN) || this.isOp
}

/**
 * 플레이어가 계정 연결 권한을 가지고 있는지 확인합니다.
 * 
 * @return Boolean 연결 권한이 있으면 true
 */
fun Player.canLinkAccount(): Boolean {
    return this.hasPermission(PermissionConstants.LINK)
}

/**
 * 플레이어가 계정 연결 해제 권한을 가지고 있는지 확인합니다.
 * 
 * @return Boolean 연결 해제 권한이 있으면 true
 */
fun Player.canUnlinkAccount(): Boolean {
    return this.hasPermission(PermissionConstants.UNLINK)
}

/**
 * 플레이어가 상태 확인 권한을 가지고 있는지 확인합니다.
 * 
 * @return Boolean 상태 확인 권한이 있으면 true
 */
fun Player.canCheckStatus(): Boolean {
    return this.hasPermission(PermissionConstants.STATUS)
}

/**
 * 플레이어가 쿨다운 우회 권한을 가지고 있는지 확인합니다.
 * 
 * @return Boolean 쿨다운 우회 권한이 있으면 true
 */
fun Player.canBypassCooldown(): Boolean {
    return this.hasPermission(PermissionConstants.BYPASS_COOLDOWN)
}

/**
 * 플레이어가 다중 계정 연결 권한을 가지고 있는지 확인합니다.
 * 
 * @return Boolean 다중 계정 권한이 있으면 true
 */
fun Player.canHaveMultipleAccounts(): Boolean {
    return this.hasPermission(PermissionConstants.MULTIPLE_ACCOUNTS)
}

/**
 * 치지직 플러그인 접두어가 포함된 일반 메시지를 전송합니다.
 * 
 * @param message 전송할 메시지
 */
fun Player.sendChzzkMessage(message: String) {
    this.sendMessage("§7[§bChzzk§7] $message")
}

/**
 * 치지직 플러그인 접두어가 포함된 오류 메시지를 전송합니다.
 * 
 * @param message 전송할 오류 메시지
 */
fun Player.sendChzzkError(message: String) {
    this.sendMessage("§7[§bChzzk§7] §c$message")
}

/**
 * 치지직 플러그인 접두어가 포함된 성공 메시지를 전송합니다.
 * 
 * @param message 전송할 성공 메시지
 */
fun Player.sendChzzkSuccess(message: String) {
    this.sendMessage("§7[§bChzzk§7] §a$message")
}

/**
 * 치지직 플러그인 접두어가 포함된 경고 메시지를 전송합니다.
 * 
 * @param message 전송할 경고 메시지
 */
fun Player.sendChzzkWarning(message: String) {
    this.sendMessage("§7[§bChzzk§7] §e$message")
}

/**
 * 치지직 이름이 포함된 플레이어 표시명을 생성합니다.
 * 
 * @param chzzkName 치지직 사용자명 (선택적)
 * @return String 포맷팅된 표시명
 */
fun Player.getDisplayNameWithChzzk(chzzkName: String? = null): String {
    return if (chzzkName != null) {
        "§7${this.name} §8(§b$chzzkName§8)"
    } else {
        "§7${this.name}"
    }
}