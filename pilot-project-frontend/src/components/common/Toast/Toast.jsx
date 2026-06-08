import { toast, Bounce } from "react-toastify";
import "./Toast.css";

const options = {
  position: "top-center",
  autoClose: 2500,
  hideProgressBar: false,
  closeOnClick: false,
  pauseOnHover: false,
  pauseOnFocusLoss: false,
  draggable: true,
  progress: undefined,
  theme: "light",
  transition: Bounce,
};

export const successToast = (message) => {
  toast.success(message, options);
};

export const errorToast = (message) => {
  toast.error(message, options);
};
