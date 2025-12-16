import { Component, OnInit, EventEmitter, Output, ChangeDetectorRef } from '@angular/core';
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

  @Output() cerrarModal = new EventEmitter();

  registroForm: FormGroup;
  registroExitoso: boolean = false;
  errorRegistro: string | null = null;

  constructor(private fb: FormBuilder,
              private usuarioService: UsuarioService,
              private cd: ChangeDetectorRef ) {
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      telefono: ['', Validators.required],
      barrio: ['', Validators.required],
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
          setTimeout(() => {
            this.cerrar();
          }, 5000);

        },
        error: (err) => {
          this.errorRegistro = 'El email ya se encuentra registrado o los datos son inválidos. Intente nuevamente.';
          this.cd.detectChanges();
        }
      });

    } else {
      this.errorRegistro = 'Complete todos los campos obligatorios.';
    }
  }

  cerrar(): void {
    this.registroForm.reset();
    this.registroExitoso = false;
    this.errorRegistro = null;
    this.cerrarModal.emit();
  }
}
