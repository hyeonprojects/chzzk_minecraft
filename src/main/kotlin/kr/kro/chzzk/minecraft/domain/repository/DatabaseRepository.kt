package kr.kro.chzzk.minecraft.domain.repository

/**
 * 데이터베이스 작업을 위한 기본 인터페이스
 * 
 * 이 인터페이스는 데이터베이스 연결 관리와 CRUD 작업을 정의합니다.
 * 모든 작업은 코루틴을 사용하여 비동기적으로 처리됩니다.
 */
interface DatabaseRepository {
    
    /**
     * 데이터베이스에 연결을 설정합니다.
     * 
     * @throws DatabaseConnectionException 연결 실패 시
     */
    suspend fun connect()
    
    /**
     * 데이터베이스 연결을 해제합니다.
     * 
     * @throws DatabaseConnectionException 연결 해제 실패 시
     */
    suspend fun disconnect()
    
    /**
     * 데이터베이스에 새로운 데이터를 삽입합니다.
     * 
     * @param data 삽입할 데이터 객체
     * @throws DatabaseOperationException 삽입 작업 실패 시
     */
    suspend fun insert(data: Any)
    
    /**
     * 기존 데이터를 업데이트합니다.
     * 
     * @param data 업데이트할 데이터 객체 (식별자 포함)
     * @throws DatabaseOperationException 업데이트 작업 실패 시
     */
    suspend fun update(data: Any)
    
    /**
     * 데이터베이스에서 데이터를 삭제합니다.
     * 
     * @param data 삭제할 데이터 객체 (식별자 포함)
     * @throws DatabaseOperationException 삭제 작업 실패 시
     */
    suspend fun delete(data: Any)
    
    /**
     * SQL 쿼리를 실행하고 결과를 반환합니다.
     * 
     * @param query 실행할 SQL 쿼리문
     * @param params 쿼리에 바인딩할 매개변수들
     * @return 쿼리 결과를 Map의 리스트로 반환 (컬럼명 -> 값)
     * @throws DatabaseQueryException 쿼리 실행 실패 시
     */
    suspend fun query(query: String, vararg params: Any?): List<Map<String, Any?>>
}