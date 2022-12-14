import React from "react";
import { Component } from "react";
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import '../resources/css/login.css';

import Swal from 'sweetalert2'

export default class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            key: ""
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
                key: ""
            }
        );
    }

    signIn = (event) => {
        event.preventDefault();

        const url = "http://localhost:8080/login/signIn";
        const user = {
            email: this.state.email,
            key: this.state.key
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
                return response.json();
            }
            )
            .then(json => {
                console.log(json);
                if (!json.uuid) {
                    Swal.fire({
                        title: json.description,
                        icon: 'warning',
                        confirmButtonText: 'Ok'
                    })
                }
                else {
                    localStorage.uuid = json.uuid;
                    localStorage.credential = json.credential;
                    localStorage.userId = json.id;
                    window.location.href="/publications";
                }
                
            })
            .catch(error => {
                console.error(error);
                localStorage.clear();
                Swal.fire({
                    title: 'Error en inicio de sesion',
                    icon: 'warning',
                    confirmButtonText: 'Ok'
                })
                
            });
        this.cleanValues();
    }

    register= (event) => {
       
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
                            onChange={this.setValues} 
                        />
                    </Form.Group>


                    <Form.Group className="mb-3 formInputs" controlId="formBasicPassword">
                        <Form.Label>Contrase??a</Form.Label>
                        <Form.Control
                            type="password"
                            controlid="key"
                            name="key"
                            className="input"
                            size="lg"
                            placeholder="Ingresa tu contrase??a"
                            required={true}
                            value={this.state.key}
                            onChange={this.setValues} 
                        />
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
                            onClick={this.signIn}>
                            Ingresar
                        </Button>
                    </span>
                </Form>
            </div>
        );
    }
}