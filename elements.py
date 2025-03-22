#!/usr/bin/env python3

# Dictionary of all 118 elements:
# The keys are the element symbols in lowercase for easy matching.
elements = {
    "h": "Hydrogen",
    "he": "Helium",
    "li": "Lithium",
    "be": "Beryllium",
    "b": "Boron",
    "c": "Carbon",
    "n": "Nitrogen",
    "o": "Oxygen",
    "f": "Fluorine",
    "ne": "Neon",
    "na": "Sodium",
    "mg": "Magnesium",
    "al": "Aluminum",
    "si": "Silicon",
    "p": "Phosphorus",
    "s": "Sulfur",
    "cl": "Chlorine",
    "ar": "Argon",
    "k": "Potassium",
    "ca": "Calcium",
    "sc": "Scandium",
    "ti": "Titanium",
    "v": "Vanadium",
    "cr": "Chromium",
    "mn": "Manganese",
    "fe": "Iron",
    "co": "Cobalt",
    "ni": "Nickel",
    "cu": "Copper",
    "zn": "Zinc",
    "ga": "Gallium",
    "ge": "Germanium",
    "as": "Arsenic",
    "se": "Selenium",
    "br": "Bromine",
    "kr": "Krypton",
    "rb": "Rubidium",
    "sr": "Strontium",
    "y": "Yttrium",
    "zr": "Zirconium",
    "nb": "Niobium",
    "mo": "Molybdenum",
    "tc": "Technetium",
    "ru": "Ruthenium",
    "rh": "Rhodium",
    "pd": "Palladium",
    "ag": "Silver",
    "cd": "Cadmium",
    "in": "Indium",
    "sn": "Tin",
    "sb": "Antimony",
    "te": "Tellurium",
    "i": "Iodine",
    "xe": "Xenon",
    "cs": "Cesium",
    "ba": "Barium",
    "la": "Lanthanum",
    "ce": "Cerium",
    "pr": "Praseodymium",
    "nd": "Neodymium",
    "pm": "Promethium",
    "sm": "Samarium",
    "eu": "Europium",
    "gd": "Gadolinium",
    "tb": "Terbium",
    "dy": "Dysprosium",
    "ho": "Holmium",
    "er": "Erbium",
    "tm": "Thulium",
    "yb": "Ytterbium",
    "lu": "Lutetium",
    "hf": "Hafnium",
    "ta": "Tantalum",
    "w": "Tungsten",
    "re": "Rhenium",
    "os": "Osmium",
    "ir": "Iridium",
    "pt": "Platinum",
    "au": "Gold",
    "hg": "Mercury",
    "tl": "Thallium",
    "pb": "Lead",
    "bi": "Bismuth",
    "po": "Polonium",
    "at": "Astatine",
    "rn": "Radon",
    "fr": "Francium",
    "ra": "Radium",
    "ac": "Actinium",
    "th": "Thorium",
    "pa": "Protactinium",
    "u": "Uranium",
    "np": "Neptunium",
    "pu": "Plutonium",
    "am": "Americium",
    "cm": "Curium",
    "bk": "Berkelium",
    "cf": "Californium",
    "es": "Einsteinium",
    "fm": "Fermium",
    "md": "Mendelevium",
    "no": "Nobelium",
    "lr": "Lawrencium",
    "rf": "Rutherfordium",
    "db": "Dubnium",
    "sg": "Seaborgium",
    "bh": "Bohrium",
    "hs": "Hassium",
    "mt": "Meitnerium",
    "ds": "Darmstadtium",
    "rg": "Roentgenium",
    "cn": "Copernicium",
    "nh": "Nihonium",
    "fl": "Flerovium",
    "mc": "Moscovium",
    "lv": "Livermorium",
    "ts": "Tennessine",
    "og": "Oganesson"
}


def find_element_segmentation(s):
    """
    Recursively try to segment the string s (assumed lowercase)
    into valid element symbols.
    Returns a list of symbols if successful, or None if not possible.
    """
    memo = {}

    def helper(i):
        if i == len(s):
            return []  # Successfully segmented all letters
        if i in memo:
            return memo[i]
        # Try one- or two-letter symbols
        for length in (1, 2):
            if i + length <= len(s):
                candidate = s[i:i+length]
                if candidate in elements:
                    remainder = helper(i + length)
                    if remainder is not None:
                        memo[i] = [candidate] + remainder
                        return memo[i]
        memo[i] = None
        return None

    return helper(0)


def main():
    # Get input from user.
    user_input = input("Enter a string to be segmented into element symbols: ").strip()
    # Remove any whitespace and convert to lowercase.
    s = user_input.replace(" ", "").lower()

    segmentation = find_element_segmentation(s)
    if segmentation is None:
        print("It is not possible to form that string using element symbols.")
    else:
        # Create a nice output list showing symbol and element name.
        output = []
        for symbol in segmentation:
            # Capitalize appropriately: first letter uppercase, second letter if exists lowercase.
            proper_symbol = symbol.capitalize()
            element_name = elements[symbol]
            output.append(f"{proper_symbol} ({element_name})")
        print("The string can be formed as:")
        print(" + ".join(output))


if __name__ == "__main__":
    main()
