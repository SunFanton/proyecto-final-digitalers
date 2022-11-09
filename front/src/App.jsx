import React, { Component } from "react"
import { BrowserRouter, NavLink, Route, Routes } from "react-router-dom";
import Login from "./components/Login";
import NotFound from "./components/NotFound";
import './resources/css/menu.css';
import './resources/css/app.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import NewPublication from "./components/NewPublication";
import PublicationsList from "./components/PublicationsList";

// ES6 Modules or TypeScript
import Swal from 'sweetalert2'

export default class App extends Component {

    constructor(props) {
        super(props);
    }
    
    printUUID=()=>{
        console.log(localStorage);
    }

    logOut = () => {
        if(localStorage.length>0){
            Swal.fire({
                title: '¿Desea cerrar sesion?',
                icon: 'info',
                confirmButtonText: 'Ok'
            }).then((result) => {
                if (result['isConfirmed']){
                    localStorage.clear();
                    window.location.href="/";
                }
            })
        }
    }

    render() {
        return (
            <div>
                <BrowserRouter>

                    <nav className="menu">
                        <NavLink className="enlace" to="/" >Principal</NavLink>
                        <NavLink className="enlace" to="/publications" >Publicaciones</NavLink>
                        <NavLink className="enlace" to="/create-publication" >Crear Publicacion</NavLink>
                        <NavLink className="enlace" onClick={this.logOut}>Cerrar Sesion</NavLink>
                    </nav>


                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/publications" element={<PublicationsList />} />
                        <Route path="/create-publication" element={<NewPublication />} />
                        <Route path="*" element={<NotFound />} />
                        <Route />
                    </Routes>

                </BrowserRouter>
            </div>
        );
    }
}