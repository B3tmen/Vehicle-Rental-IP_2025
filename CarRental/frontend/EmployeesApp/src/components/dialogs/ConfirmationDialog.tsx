import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";

interface Props{
    visible: boolean;
    onHide: () => void;
    draggable?: boolean;
    header: React.ReactNode;
    children: React.ReactNode;
    onNoAction: () => void;
    onYesAction: () => void;
}

const ConfirmationDialog: React.FC<Props> = (
    {   
        visible, 
        onHide, 
        draggable = false, 
        header, 
        onNoAction,
        onYesAction,
        children
    }) => {
    return(
        <Dialog
            visible={visible}
            onHide={onHide}
            draggable={draggable}
            header={header}
            footer={
                <>
                    <Button
                        label="No"
                        icon="pi pi-times"
                        className="p-button-danger"
                        style={{ backgroundColor: 'var(--color-red)' }}
                        onClick={onNoAction}
                    />
                    <Button
                        label="Yes"
                        icon="pi pi-check"
                        className="p-button-success"
                        style={{ backgroundColor: 'var(--color-green)' }}
                        onClick={onYesAction}
                    />
                </>
            }
        >
            {children}
        </Dialog>
    );
}

export default ConfirmationDialog;