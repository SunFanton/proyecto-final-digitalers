import React from 'react';
import { Component } from 'react';
import Card from 'react-bootstrap/Card';

export default class PublicationsList extends Component{

    constructor(props) {
        super(props);
        this.state = {
            publications: []
        }
    }

    componentDidMount() {
        this.getPublications();
    }

    getPublications = () => {
        var userId = localStorage.getItem("userId");
        const url = "http://localhost:8080/publications/findByUserId/" + userId;
        const header = {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }

        fetch(url, header)
            .then(response => {
                if (!response.ok) throw Error(response.status);
                return response.json();
            }
            )
            .then(json => this.setState({ publications: json }))
            .catch(error => {
                console.error(error);
                localStorage.clear();
                alert("Error al obtener las publicaciones. Intenta ingresar de nuevo");
                window.location.href="/";
            })
            .finally(() => console.info(this.state.publications));
    }


    render(){
        return(
            <>
                <ul>
                {
                    this.state.publications.map(
                        item => (
                            <Card key={item.id}>
                                <Card.Body>
                                    <Card.Title>{item.title}</Card.Title>
                                    <Card.Text>{item.body}</Card.Text>
                                </Card.Body>
                            </Card>
                        )
                    )
                }
                </ul>
            </>
        )
    }
}