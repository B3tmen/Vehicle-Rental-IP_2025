import { InfoDisplayConfig } from "types/types";
import { Dialog } from "primereact/dialog";

type Props<T> = {
    visible: boolean;
    onHide: () => void;
    header: string;
    data: T | undefined;
    config: InfoDisplayConfig<T>[];
};

function InfoDisplayDialog<T>({ visible, onHide, header, data, config }: Props<T>) {
    return (
        <Dialog
            header={header}
            visible={visible}
            draggable={false}
            style={{ width: "90%", maxWidth: "400px" }}
            onHide={onHide}
        >
            {data ? (
                <div>
                    {config.map(({ label, field, render }) => (
                        <p key={ String(field) }>
                            <strong>{label}:</strong>{" "}
                            {render ? render(data[field], data) : String(data[field] ?? "")}
                        </p>
                    ))}
                </div>
            ) : (
                <p>No data to display.</p>
            )}
        </Dialog>
    );
}

export default InfoDisplayDialog;