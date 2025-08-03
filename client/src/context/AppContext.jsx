import axios from "axios";
import { AppConstants } from "../util/constants";
import { createContext, useEffect, useState } from "react";
import { toast } from "react-toastify";

export const AppContext = createContext();
export const AppContextProvider = (props) =>{

    axios.defaults.withCredentials=true;

    const backendURL = AppConstants.BACKEND_URL;
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userData, setUserData] = useState(false);
    const getUserData = async() => {
        try {
            const response = await axios.get(`${backendURL}/profile`);
            if (response.status === 200) {
                setUserData(response.data);
            }else{
                toast.error("Unable to retrieve profile");
            }
        } catch (error) {
            toast.error(error.message);
        }
    }
    
    const getAuthState = async() => {
        try{
            const response = await axios.get(backendURL+"/is-authenticated");
            if(response.status === 200 && response.data === true){
                setIsLoggedIn(true);
                await getUserData();
            }else{
                setIsLoggedIn(false);
            }
        }catch(error){
            if (error.response?.status === 401) {
                setIsLoggedIn(false);
            } else {
                const msg = error.response?.data?.message || "Authentication check failed";
                toast.error(msg);
                setIsLoggedIn(false);
            }
        }
    }
    useEffect(() => {
    const token = localStorage.getItem("jwt-login-attempt"); // or similar
    if (token) {
        getAuthState();
    }
}, []);

    
    const contextValue = {
        backendURL,
        isLoggedIn,setIsLoggedIn,
        userData, setUserData,
        getUserData,
    }
    return(
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    )
}