package lineage;

import sqlexecutor.pojo.DatabaseTypeEnum;

/**
 * 解析sql,生成LineageInfo
 *
 * @author maolh
 * @date 2019/06/26
 */
public class LineageInfoParserUtils {

    /**
     * @param dbType
     * @return
     */
    public static LineageInfoParser createLineageInfoParser(int dbType) {
        if (DatabaseTypeEnum.ORACLE.getCode().equals(dbType)) {
            return new OracleLineageInfoParser();
        }

        return null;
    }


}
