import pdfplumber
import pandas as pd
import re

def extract_but_syllabus(pdf_path):
    all_resources = []
    current_item = None
    
    print("Analyse du PDF en cours... (on ignore le sommaire)")

    with pdfplumber.open(pdf_path) as pdf:
        for i, page in enumerate(pdf.pages):
            # On ignore les 50 premières pages (le sommaire)
            if i < 50: 
                continue
            
            # MAGIE ICI : x_tolerance=1.5 force la détection des espaces entre les mots !
            text = page.extract_text(x_tolerance=1.5, y_tolerance=3)
            if not text: continue
            
            lines = text.split('\n')
            for line in lines:
                line = line.strip()
                if not line:
                    continue
                
                # Ignorer les lignes de sommaire qui traînent
                if re.search(r'\.\s*\.\s*\.\s*\d+$', line):
                    continue
                
                # Cherche les titres de cours
                match = re.search(r"(Ressource\s*R[a-zA-Z0-9.]+|SA[EÉ]\s*\d\.\d+)\s*[:]\s*(.*)", line, re.IGNORECASE)
                
                if match:
                    if current_item:
                        all_resources.append(current_item)
                    
                    code_brut = match.group(1)
                    # Nettoyage du code
                    code = re.sub(r'Ressource\s*', '', code_brut, flags=re.IGNORECASE).strip()
                    code = code.replace('SAE', 'SAÉ')
                    
                    titre = match.group(2).strip()
                    
                    current_item = {
                        "Code": code,
                        "Titre": titre,
                        "Descriptif": "",
                        "Savoirs": [],
                        "AC": [],
                        "Volume": ""
                    }
                    continue

                if current_item:
                    # Capture du descriptif
                    if re.search(r"Descriptif", line, re.IGNORECASE) and ":" in line:
                        parts = line.split(":", 1)
                        if len(parts) > 1:
                            current_item["Descriptif"] = parts[1].strip()
                    
                    # Capture des AC (On cherche le format "AC xx.xx")
                    elif re.search(r'AC\s*\d\d\.\d\d', line):
                        current_item["AC"].append(line.strip())
                    
                    # Capture des savoirs (puces)
                    elif re.match(r'^[-–•]', line):
                        content = line[1:].strip()
                        if len(content) > 3:
                            current_item["Savoirs"].append(content)
                    
                    # Capture du volume horaire
                    elif "Volume horaire" in line or "Volumehoraire" in line:
                        if ":" in line:
                            parts = line.split(":", 1)
                            current_item["Volume"] = parts[1].strip()
                        else:
                            current_item["Volume"] = line

    # Ajouter le tout dernier cours
    if current_item:
        all_resources.append(current_item)

    df = pd.DataFrame(all_resources)
    
    if df.empty:
        print("Erreur : Aucune donnée trouvée.")
        return df
    
    # Transformation des listes pour le CSV (séparées par | )
    df['Savoirs'] = df['Savoirs'].apply(lambda x: " | ".join(x) if isinstance(x, list) else x)
    df['AC'] = df['AC'].apply(lambda x: " | ".join(x) if isinstance(x, list) else x)
    
    return df

# --- EXÉCUTION ---
file_path = "annexe-2-licence-professionnelle-bachelor-universitaire-de-technologie-informatique-29016 (1).pdf"
df_final = extract_but_syllabus(file_path)

if not df_final.empty:
    df_final.to_csv("syllabus_but_informatique.csv", index=False, encoding='utf-8-sig', sep=';')
    print(f"\n[OK] Succès ! {len(df_final)} ressources ont été extraites avec les espaces.")