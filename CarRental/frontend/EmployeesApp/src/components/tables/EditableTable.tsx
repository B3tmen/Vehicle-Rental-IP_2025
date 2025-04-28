import { useEffect, useState } from "react";
import { Button } from "primereact/button";
import ColumnConfig, { CustomDataTable } from "@components/tables/CustomDataTable";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPen, faTrashAlt } from "@fortawesome/free-solid-svg-icons";
import ConfirmationDialog from "@components/dialogs/ConfirmationDialog";


interface EditableTableProps<T> {
    data: T[];
    dataKey: string;
    columns: ColumnConfig<T>[];
    //additionalActions?: React.ReactNode;
    onDelete?: (item: T) => void;
    //onUpdate?: (item: T, formData?: FormData) => void;
    onShowUpdateModal?: (item: T) => void;
    header: React.ReactNode;
    rows?: number;
    rowsPerPageOptions?: number[];
    filters?: any;
}


const EditableTable = <T extends { id: number | null }>({
    data,
    dataKey,
    columns,
    //additionalActions,
    onDelete,
    onShowUpdateModal,
    header,
    rows = 5,
    rowsPerPageOptions = [5, 10, 15, 20],
    filters
}: EditableTableProps<T>) => {
    const [tableData, setTableData] = useState<T[]>(data);
    const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
    const [rowToDelete, setRowToDelete] = useState<T | null>(null);
    
    useEffect(() => {
        setTableData(data);
    }, [data]); // Update tableData whenever data changes

    const startEditing = (id: number | null, rowData: T) => {
        //setEditingRowId(id);
        onShowUpdateModal?.(rowData);
        //setEditedData(rowData); // Load current row data into the editing state
    };

    const showDeleteDialog = (rowData: T) => {
        setRowToDelete(rowData);
        setDeleteDialogVisible(true);
    };

    const confirmDelete = () => {
        if (rowToDelete) {
            const updatedData = tableData.filter((item) => item.id !== rowToDelete.id);
            setTableData(updatedData);
            onDelete?.(rowToDelete); // Call delete callback if provided
        }
        setDeleteDialogVisible(false);
        setRowToDelete(null);
    };

    const cancelDelete = () => {
        setDeleteDialogVisible(false);
        setRowToDelete(null);
    };

    const actionColumnBody = (rowData: T) => {
        return (
            <div>
                <Button
                    icon={<FontAwesomeIcon icon={faPen} />}
                    text
                    outlined
                    tooltip="Edit"
                    style={{ color: 'var(--color-secondary)' }}
                    onClick={() => startEditing(rowData.id, rowData)}
                />
                <Button
                    icon={<FontAwesomeIcon icon={faTrashAlt} />}
                    text
                    outlined
                    tooltip="Delete"
                    style={{ color: 'var(--color-red)' }}
                    onClick={() => showDeleteDialog(rowData)}
                />
            </div>
        );
    };

    const dynamicColumns = columns.map((col) => ({
            ...col,
            body: col.body
        })
    );

    dynamicColumns.push({
        header: "Actions",
        body: actionColumnBody,
    });

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
            <ConfirmationDialog
                visible={deleteDialogVisible}
                onHide={cancelDelete}
                header="Delete confirmation"
                onNoAction={cancelDelete}
                onYesAction={confirmDelete}
            >
                <p>Are you sure you want to delete this item?</p>
            </ConfirmationDialog>
        </>
    );
};

export default EditableTable;