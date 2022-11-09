import React from 'react';
import { Component } from 'react';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash, faFolder, faPencil } from "@fortawesome/free-solid-svg-icons";
import '../resources/css/publication.css';

// ES6 Modules or TypeScript
import Swal from 'sweetalert2'


export default class Publication extends Component{

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.id,
            title: this.props.title,
            body: this.props.body
        }
    }

    deletePublication = (event) => {
        Swal.fire({
            title: '¿Desea eliminar esta publicacion?',
            icon: 'info',
            confirmButtonText: 'Ok'
          }).then((result) => {
            if (result['isConfirmed']){
                
                const url = "http://localhost:8080/publications/delete";
                const publication = {
                    id: this.state.id,
                    title: this.state.title,
                    body: this.state.body
                }
                const header = {
                    method: "DELETE",
                    body: JSON.stringify(publication),
                    headers: {
                        "Content-Type": "application/json",
                        "credential": localStorage.getItem("uuid")
                    }
                }

                fetch(url, header)
                    .then(response => {
                        if (!response.ok) throw Error(response.status);
                        return response.json();
                    }
                    )
                    .then(json => {
                        console.log(json);
                        Swal.fire({
                            title: 'Publicacion eliminada',
                            icon: 'info',
                            confirmButtonText: 'Ok'
                        }).then((result) => {
                            if (result['isConfirmed']){
                                window.location.href="/publications";
                            }
                        })
                        
                    })
                    .catch(error => {
                        console.error(error);
                        Swal.fire({
                            title: 'Error al eliminar la publicacion',
                            icon: 'warning',
                            confirmButtonText: 'Ok'
                        }) 
                    });
            }
            })
        
    }

    render() {
        return(
            <>
                <Card className="card" key={this.state.id}>
                    <Card.Body className="cardBody">
                        <Card.Title className="cardTitle">
                            <div className="titleDiv">
                                <FontAwesomeIcon icon={faFolder} className="primary"/>
                                <h2 className="title">{this.state.title}</h2>
                            </div>
                        </Card.Title>
                        <Card.Text className="cardtext">
                            {this.state.body}
                        </Card.Text>
                    </Card.Body>
                    <div className="buttonsCard">
                        <Button variant="danger" onClick={this.deletePublication}>
                            <FontAwesomeIcon icon={faTrash} />
                        </Button>
                    </div>
                </Card>
            </>
        );
    }
}