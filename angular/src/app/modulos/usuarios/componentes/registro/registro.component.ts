import { Component, OnInit,EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../../../services/usuario.service';
import { RegistroRequest } from '../../../../models/auth-request.interface';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  @Output() cerrarModal = new EventEmitter<void>();

  registroForm: FormGroup;
  registroExitoso: boolean = false;
  errorRegistro: string | null = null;

  constructor(private fb: FormBuilder, private usuarioService: UsuarioService) {
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      telefono: ['', Validators.required],
      barrio: [''],
      ciudad: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    this.registroExitoso = false;
    this.errorRegistro = null;

    if (this.registroForm.valid) {
      const data: RegistroRequest = this.registroForm.value;
      this.usuarioService.registrarUsuario(data).subscribe({
        next: (response) => {
          this.registroExitoso = true;
        },
        error: (err) => {
          this.errorRegistro = 'Error al registrar. Intente nuevamente.';
        }
      });
    } else {
      this.errorRegistro = 'Complete todos los campos obligatorios.';
    }
  }

  cerrar(): void {
    this.cerrarModal.emit();
  }
}
