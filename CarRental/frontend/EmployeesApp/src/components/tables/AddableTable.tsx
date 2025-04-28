import { useEffect, useState } from "react";
import ColumnConfig, { CustomDataTable } from "@components/tables/CustomDataTable";

interface Props<T>{
    data: T[];
    dataKey: string;
    columns: ColumnConfig<T>[];
    onAdd?: (item: T) => void;
    header: React.ReactNode;
    rows?: number;
    rowsPerPageOptions?: number[];
    filters?: any;
    actionBody?: React.ReactNode;
}

const AddableTable = <T extends { id: number | null }>({
    data,
    dataKey,
    columns,
    onAdd,
    header,
    rows = 5,
    rowsPerPageOptions = [5, 10, 15, 20],
    filters,
}: Props<T>) => {
    const [tableData, setTableData] = useState<T[]>(data);

    useEffect(() => {
        setTableData(data);
    }, [data]);


    const dynamicColumns = columns.map((col) => ({
        ...col,
        body: col.body
    }));

    return (
        <>
            <CustomDataTable
                value={tableData}
                dataKey={dataKey}
                columns={dynamicColumns}
                header={header}
                paginator
                rows={rows}
                rowsPerPageOptions={rowsPerPageOptions}
                filters={filters}
            />
        
        </>
    );
}

export default AddableTable;