import React from "react";
import { Component } from "react";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import '../resources/css/login.css';

import Swal from 'sweetalert2'

export default class Register extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            key: "",
            repeatKey: ""
        }
    }


    setValues = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    cleanValues = () => {
        this.setState(
            {
                email: "",
                key: "",
                repeatKey: ""
            }
        );
    }

    register = (event) => {
        event.preventDefault();

        if(this.state.key !== this.state.repeatKey){
            this.cleanValues();
            Swal.fire({
                title: 'Las contraseñas deben ser iguales',
                icon: 'warning',
                confirmButtonText: 'Ok'
            })
            return;
        }

        const url = "http://localhost:8080/users/insertNewUser";
        const user = {
            email: this.state.email,
            key: this.state.key,
            active: true,
            message: "New User"
        }
        const header = {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
                "Content-Type": "application/json"
            }
        }

        fetch(url, header)
            .then(response => {
                if (!response.ok) throw Error(response.status);
                Swal.fire({
                    title: '¡Registro exitoso!',
                    icon: 'success',
                    confirmButtonText: 'Ok'
                })
                return response.json();
            }
            )
            .then(json => {
                console.log(json);
                window.location.href="/";
            })
            .catch(error => {
                console.error(error);
                localStorage.clear();
                Swal.fire({
                    title: 'Error de registro. Intenta de nuevo con otro email' ,
                    icon: 'warning',
                    confirmButtonText: 'Ok'
                })
                
            });
        this.cleanValues();
    }

    render() {
        return (
            <div>
                <Form className="formLogin" >
                    <Form.Group className="mb-3 formInputs" controlId="formBasicEmail">
                        <Form.Label>Email </Form.Label>
                        <Form.Control
                            type="email"
                            controlid="email"
                            name="email"
                            className="input"
                            size="lg"
                            placeholder="Ingresa tu email"
                            required={true}
                            value={this.state.email}
                            onChange={this.setValues} />
                    </Form.Group>


                    <Form.Group className="mb-3 formInputs" controlId="formBasicPassword">
                        <Form.Label>Contraseña</Form.Label>
                        <Form.Control
                            type="password"
                            controlid="key"
                            name="key"
                            className="input"
                            size="lg"
                            placeholder="Ingresa tu contraseña"
                            required={true}
                            value={this.state.key}
                            onChange={this.setValues} />
                    </Form.Group>

                    <Form.Group className="mb-3 formInputs" controlId="formBasicPassword">
                        <Form.Label>Repite contraseña</Form.Label>
                        <Form.Control
                            type="password"
                            controlid="repeatKey"
                            name="repeatKey"
                            className="input"
                            size="lg"
                            placeholder="Ingresa tu contraseña"
                            required={true}
                            value={this.state.repeatKey}
                            onChange={this.setValues} />
                    </Form.Group>

                    <span className="div-login-buttons">
                        <Button
                            variant="secondary"
                            type="reset"
                            className="formButton"
                            size="lg"
                            onClick={this.cleanValues}>
                            Limpiar
                        </Button>
                        <Button
                            variant="primary"
                            type="submit"
                            className="formButton"
                            size="lg"
                            onClick={this.register}>
                            Registrar
                        </Button>
                    </span>
                </Form>
            </div>
        );
    }
}