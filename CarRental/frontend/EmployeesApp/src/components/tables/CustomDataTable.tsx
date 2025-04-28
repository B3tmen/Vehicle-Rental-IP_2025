import { Column } from "primereact/column";
import { DataTable, DataTableValue } from "primereact/datatable";

interface ColumnConfig<T> {
    field?: keyof T; // Field must be a key of T if specified
    header: string; // Header is required
    body?: (rowData: T) => React.ReactNode; // Optional custom renderer
    style?: React.CSSProperties; // Optional column style
}
export default ColumnConfig;

interface Props<T extends DataTableValue>{
    value: T[];
    columns: ColumnConfig<T>[];
    dataKey: string;
    header?: React.ReactNode;
    paginator?: boolean;
    rows?: number;
    rowsPerPageOptions?: number[];
    filters?: any;
    emptyMessage?: string;
}

export const CustomDataTable = <T extends DataTableValue,>({
    value,
    columns,
    dataKey,
    header,
    paginator = false,
    rows = 10,
    rowsPerPageOptions = [5, 10, 20],
    filters,
    emptyMessage = "No records found",
}: Props<T>) => {

    return(
        <DataTable
            value={value}
            dataKey={dataKey}
            header={header}
            paginator={paginator}
            rows={rows}
            rowsPerPageOptions={rowsPerPageOptions}
            filters={filters}
            emptyMessage={emptyMessage}
        >
            {columns.map((col, index) => (
                <Column
                    key={index}
                    field={col.field ? String(col.field) : undefined} // Convert field to string if it exists
                    header={col.header}
                    body={col.body}
                    style={col.style}
                />
            ))}
        </DataTable>
    );
}